<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="封面"/>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary">
              编辑
            </a-button>
            <a-button type="primary" danger>
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>
<script setup lang="ts">

import {defineComponent, onMounted, ref} from "vue";
import axios from "axios";

defineComponent({
  name: "AdminEbook"
})

const ebooks = ref();
const pagination = ref({
  current: 1,
  pageSize: 2,
  total: 0
});
const loading = ref(true);

const columns = [
  {
    title: "封面",
    dataIndex: "cover",
    slots: {customRender: "cover"}
  },
  {
    title: "名称",
    dataIndex: "name",
  },
  {
    title: "分类一",
    key: "category1Id",
    dataIndex: "category1Id",
  },
  {
    title: "分类二",
    dataIndex: "category2Id",
  },
  {
    title: "文档数",
    dataIndex: "docCount",
  },
  {
    title: "阅读数",
    dataIndex: "viewCount",
  },
  {
    title: "点赞数",
    dataIndex: "voteCount",
  },
  {
    title: "操作",
    dataIndex: "action",
    slots: {customRender: "action"}
  }
];

/**
 * 查询请求
 *
 * @param params
 */
const handleQuery = (params: any) => {
  axios.get("/ebook/list", {
    params
  }).then(res => {
    loading.value = false;
    ebooks.value = res.data.content.list;

    pagination.value.current = params.page;
    pagination.value.total = res.data.content.list;
  })
}

/**
 * 换页
 *
 * @param pagination
 */
const handleTableChange = (pagination: any) => {
  console.log("自带分页参数 : " + pagination);
  handleQuery({
    page: pagination.current,
    size: pagination.pageSize
  });
}

onMounted(() => {
  handleQuery({
    page: 1,
    size: pagination.value.pageSize
  });
})

</script>