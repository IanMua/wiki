<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          :items="items"
          @click="handleClickMenu"
      >
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-list item-layout="vertical" size="default" :data-source="ebooks" :grid="{ gutter: 20, column: 3 }"
              :loading="loading">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
            <span v-for="{ icon, text } in actions" :key="icon">
              <component :is="icon" style="margin-right: 8px"/>
              {{ text }}
            </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id + '&ebookName=' + item.name">
                  {{ item.name }}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script setup lang="ts">

import {defineComponent, h, onMounted, ref, VueElement} from 'vue';
import axios from "axios";
import {Tool} from "@/util/tool";
import {ItemType} from "ant-design-vue";
import {MailOutlined} from "@ant-design/icons-vue";

defineComponent({
  name: 'HomeView',
});

const actions = [
  {
    icon: 'StarOutlined',
    text: '156'
  },
  {
    icon: 'LikeOutlined',
    text: '156'
  },
  {
    icon: 'MessageOutlined',
    text: '2'
  }
];

let ebooks: any = ref([]);

/**
 * 菜单数据转换
 */
const items = ref([]);

function getItem(
    label: VueElement | string,
    key: string,
    icon?: any,
    children?: any,
    type?: 'group',
): ItemType {
  return {
    key,
    icon,
    children,
    label,
    type,
  } as ItemType;
}

const loading = ref(true);
const categorys = ref();
const level1 = ref();
/**
 * 查询全部的分类
 * @param params
 */
const handleQueryCategory = (params?: any) => {
  return new Promise((resolve, reject) => {
    loading.value = true;
    axios.get("/category/all", {
      params
    }).then(res => {
      loading.value = false;

      if (!res.data.success) {
        return;
      }

      categorys.value = res.data.content;

      level1.value = Tool.array2Tree(categorys.value, categorys.value[0].parent);

      let t: any = [];
      t.push(getItem("全部", "0", () => h(MailOutlined)));
      level1.value.forEach((res: any) => {
        let arrays: ItemType[] = [];
        res.children.forEach((i: any) => {
          arrays.push(getItem(i.name, i.id));
        })
        t.push(getItem(res.name, res.id, () => h(MailOutlined), arrays));
      })
      items.value = t;

      resolve(res);
    });
  });
}

/**
 * 菜单查询请求
 *
 * @param param
 */
const handleClickMenu = (param: any) => {
  if (param.keyPath[0] === "0") {
    handleQueryEbookAll();
    return;
  }

  const category1Id = param.keyPath[0];
  const category2Id = param.keyPath[1];

  handleQueryEbookAll({
    category1Id,
    category2Id
  })
  console.log(param)
}

/**
 * 电子书查询请求
 *
 * @param params
 */
const handleQueryEbookAll = (params?: any) => {
  return new Promise((resolve, reject) => {
    loading.value = true;
    axios.get("/ebook/all", {
      params
    }).then(res => {
      loading.value = false;

      if (res === undefined) {
        return;
      }

      if (!res.data.success) {
        return;
      }

      ebooks.value = res.data.content;

      resolve(res);
    })
  })
}

onMounted(() => {
  handleQueryEbookAll();
  handleQueryCategory();
})

</script>

<style scoped>

.ant-avatar {
  width: 50px;
  height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}

</style>
