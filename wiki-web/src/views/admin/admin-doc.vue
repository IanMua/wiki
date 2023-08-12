<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div style="margin-bottom: 10px">
        <a-space>
          <a-input-search v-model:value="searchBookName"
                          allow-clear
                          :loading="searching"
                          enter-button
                          placeholder="文档名称"
                          @search="handleSearchBookName"
          >
            <template #prefix>
              <book-two-tone/>
            </template>
            <template #enter-button>

            </template>
          </a-input-search>
          <a-button type="primary" @click="add">
            添加
          </a-button>
        </a-space>
      </div>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
          :loading="loading"
          :pagination="false"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'cover'">
            <img :src="record.cover" alt="封面"/>
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-popconfirm
                  title="删除后不可恢复，确认删除?"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="primary" danger>
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal v-model:open="formOpen" title="文档表单" :confirm-loading="formLoading" @ok="handleFormOk">
    <a-form
        :model="doc"
        name="basic"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 16 }"
    >

      <a-form-item
          label="名称"
          name="name"
          :rules="[{ required: true, message: '请输入文档名称' }]"
      >
        <a-input v-model:value="doc.name"/>
      </a-form-item>

      <a-form-item
          label="父文档"
          name="name"
          :rules="[{ required: true, message: '请输入文档名称' }]"
      >
        <a-tree-select
            v-model:value="doc.parent"
            show-search
            style="width: 100%"
            :field-names="{label: 'name', value: 'id', children: 'children'}"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            placeholder="请选择父文档"
            allow-clear
            tree-default-expand-all
            :tree-data="treeSelectData"
            tree-node-filter-prop="label"
        />
      </a-form-item>

      <a-form-item
          label="顺序"
          name="sort"
          :rules="[{ required: true, message: '请输入排序号' }]"
      >
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<script setup lang="ts">

import {defineComponent, h, onMounted, ref} from "vue";
import axios from "axios";
import {notification, NotificationPlacement} from "ant-design-vue";
import {CloseCircleFilled} from "@ant-design/icons-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

defineComponent({
  name: "AdminDoc"
})

const route = useRoute();

const docs = ref();

const loading = ref(true);

const columns = [
  {
    title: "名称",
    dataIndex: "name"
  },
  {
    title: "父文档",
    key: "parent",
    dataIndex: "parent",
  },
  {
    title: "顺序",
    dataIndex: "sort",
  },
  {
    title: "操作",
    dataIndex: "action"
  }
];

/**
 * 表单
 */
const doc: any = ref();
const formOpen = ref(false);
const formLoading = ref(false);
const treeSelectData = ref();
const handleFormOk = () => {
  formLoading.value = true;
  axios.post("/doc/save", {
    ...doc.value
  }).then(res => {
    if (res.data.success) {
      formLoading.value = false;
      formOpen.value = false;

      handleQuery();
    } else {
      formLoading.value = false;
    }
  })
}
const setDisable = (treeSelectData: any, id: any) => {
  for (let i = 0; i < treeSelectData.length; i++) {
    const node = treeSelectData[i];
    if (node.id === id) {
      node.disabled = true;

      const children = node.children;
      if (Tool.isNotEmpty(children)) {
        for (let j = 0; j < children.length; j++) {
          setDisable(children, children[j].id);
        }
      }
    }
  }
}

const ids: Array<string> = [];
const getDeleteIds = (treeSelectData: any, id: any) => {
  for (let i = 0; i < treeSelectData.length; i++) {
    const node = treeSelectData[i];
    if (node.id === id) {
      ids.push(id);

      const children = node.children;
      if (Tool.isNotEmpty(children)) {
        for (let j = 0; j < children.length; j++) {
          getDeleteIds(children, children[j].id);
        }
      }
    }
  }
}

/**
 * 编辑
 */
const edit = (record: any) => {
  formOpen.value = true;
  doc.value = Tool.copy(record);

  treeSelectData.value = Tool.copy(level1.value);
  setDisable(treeSelectData.value, record.id);
  treeSelectData.value.unshift({id: 0, name: "无"});
}

/**
 * 添加
 */
const add = () => {
  formOpen.value = true;
  doc.value = {
    ebookId: route.query.ebookId
  };

  treeSelectData.value = Tool.copy(level1.value);
  treeSelectData.value.unshift({id: 0, name: "无"});
}

/**
 * 删除
 */
const handleDelete = (id: number) => {
  getDeleteIds(level1.value, id);
  axios.delete("/doc/delete/" + ids.join(",")).then(res => {
    if (res.data.success) {
      handleQuery();
    }
  });
};

/**
 * 书名搜索
 */
const searchBookName = ref("");
const searching = ref(false);
const handleSearchBookName = () => {
  if (searching.value === true) {
    return;
  }
  searching.value = true;
  handleQuery({
    name: searchBookName.value
  }).then(() => {
    searching.value = false;
  })
}

const level1 = ref();

/**
 * 查询请求
 */
const handleQuery = (params?: any) => {
  return new Promise((resolve, reject) => {
    loading.value = true;
    axios.get("/doc/all", {
      params
    }).then(res => {
      loading.value = false;

      if (!res.data.success) {
        return;
      }

      docs.value = res.data.content;

      level1.value = Tool.array2Tree(docs.value, 0);

      resolve(res);
    })
  })
}

onMounted(() => {
  handleQuery();
});

</script>