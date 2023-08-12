<script setup lang="ts">
import {defineComponent, h, onMounted, ref} from "vue";
import axios from "axios";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import {notification} from "ant-design-vue";
import {CloseCircleTwoTone} from "@ant-design/icons-vue";

defineComponent({
  name: "Doc"
})

/**
 * 数据定义
 */
const route = useRoute();

/**
 * 文档书选择节点
 */
const onDocTreeSelect = (info: any) => {
  handleQueryDocContent(info[0]).then((res: any) => {
    if (res.code !== undefined) {
      notification.error({
        message: res.msg,
        description: res.msg
      });
    }
    docContentLoading.value = false;
  })
}

/**
 * 查询文档树
 */
//文档树加载是否完成
const docTreeLoading = ref(true);
//加载完成延迟毫秒数
const docTreeLoadingDelay = 1000;
//文档数初始数据
const docs: any = ref();
//文档树
const docTree = ref();
const handleQueryDocTree = (params?: any) => {
  return new Promise((resolve, reject) => {
    docTreeLoading.value = true;
    setTimeout(() => {
      resolve({code: 400, msg: "请求超时"});
    }, 10000);
    axios.get("/doc/all", {
      params
    }).then((res: any) => {
      if (!res.data?.success) {
        resolve(res);
      }

      docs.value = res.data.content;
      docTree.value = Tool.array2Tree(docs.value, 0);

      resolve(res);
    })
  })
}

/**
 * 查询文档内容
 */
const docContent = ref();
const docContentLoading = ref(true);
const handleQueryDocContent = (id: string) => {
  return new Promise((resolve, reject) => {
    docContentLoading.value = true;
    setTimeout(() => {
      resolve({code: 400, msg: "请求超时"});
    }, 10000);
    axios.get("/doc/query-content/" + id).then((res: any) => {
      if (!res.data?.success) {
        resolve(res);
      }

      docContent.value = res.data.content;

      resolve(res);
    })
  })
}

onMounted(async () => {
  docTreeLoading.value = true;
  await handleQueryDocTree({
    ebookId: route.query.ebookId
  }).then((res: any) => {
    if (res.code !== undefined) {
      notification.error({
        message: res.msg,
        description: res.msg
      });
    }
    docTreeLoading.value = false;
  });
  let id: string = docTree.value[0].id
  await handleQueryDocContent(id).then((res: any) => {
    if (res.code !== undefined) {
      notification.error({
        message: res.msg,
        description: res.msg
      });
    }
    docContentLoading.value = false;
  })
});

</script>

<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row>
        <a-col :span="6">
          <a-spin :spinning="docTreeLoading">
            <div class="doc-tree-title">{{ route.query.ebookName ? route.query.ebookName : "" }}</div>
            <a-tree
                v-if="!docTreeLoading"
                :tree-data="docTree"
                :field-names="{children: 'children', title: 'name', key: 'id'}"
                :default-expand-all="true"
                @select="onDocTreeSelect"
            >
            </a-tree>
            <a-empty
                v-else
            >
              <template #description>
                "暂无数据"
              </template>
            </a-empty>
          </a-spin>
        </a-col>
        <a-col :span="18">
          <div :innerHTML="docContent"></div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<style scoped>
.doc-tree-title {
  font-size: 16px;
  margin-bottom: 10px;
  font-weight: bold;
}
</style>