<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div style="margin-bottom: 10px">
        <a-space>
<!--          <a-input-search v-model:value="searchBookName"-->
<!--                          allow-clear-->
<!--                          :loading="searching"-->
<!--                          enter-button-->
<!--                          placeholder="电子书名称"-->
<!--                          @search="handleSearchBookName"-->
<!--          >-->
<!--            <template #prefix>-->
<!--              <book-two-tone/>-->
<!--            </template>-->
<!--            <template #enter-button>-->

<!--            </template>-->
<!--          </a-input-search>-->
          <a-button type="primary" @click="add">
            添加
          </a-button>
        </a-space>
      </div>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'action'">
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
  <a-modal v-model:open="formOpen" title="用户表单" :confirm-loading="formLoading" @ok="handleFormOk" ok-text="确认"
           cancel-text="取消">
    <a-form
        :model="user"
        name="basic"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 16 }"
    >
      <a-form-item
          label="登录名"
          name="loginName"
          :rules="[{ required: true, message: '请输入登录名'}]"
      >
        <a-input v-model:value="user.loginName"/>
      </a-form-item>

      <a-form-item
          label="昵称"
          name="name"
          :rules="[{ required: true, message: '请输入昵称' }]"
      >
        <a-input v-model:value="user.name"/>
      </a-form-item>

      <a-form-item
          label="密码"
          name="password"
          :rules="[{ required: true, message: '请输入密码' }]"
      >
        <a-input v-model:value="user.password"/>
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

defineComponent({
  name: "AdminUser"
})

const users = ref();

const loading = ref(true);

const columns = [
  {
    title: "登录名",
    dataIndex: "loginName",
  },
  {
    title: "昵称",
    dataIndex: "name"
  },
  {
    title: "密码",
    dataIndex: "password",
  },
  {
    title: "操作",
    dataIndex: "action"
  }
];

/**
 * 表单
 */
const user: any = ref();
const formOpen = ref(false);
const formLoading = ref(false);
const handleFormOk = () => {

  formLoading.value = true;
  axios.post("/user/save", {
    ...user.value
  }).then(res => {
    if (res.data.success) {
      formLoading.value = false;
      formOpen.value = false;

      handleQueryUser({
        page: pagination.value.current,
        size: pagination.value.pageSize
      });
    } else {
      formLoading.value = false;
    }
  })
}

/**
 * 编辑
 */
const edit = (record: any) => {
  formOpen.value = true;
  user.value = Tool.copy(record);
}

/**
 * 添加
 */
const add = () => {
  formOpen.value = true;
  user.value = {};
}

/**
 * 删除
 */
const handleDelete = (id: number) => {
  axios.delete(`/ebook/delete/${id}`).then(res => {
    if (res.data.success) {
      handleQueryUser({
        page: pagination.value.current,
        size: pagination.value.pageSize
      });
    }
  });
};

/**
 * 查询用户
 *
 * @param params
 */
const handleQueryUser = (params: any) => {
  return new Promise((resolve, reject) => {
    loading.value = true;
    axios.get("/user/list", {
      params
    }).then(res => {

      if (res === undefined) {
        return;
      }

      if (!res.data.success) {
        return;
      }

      users.value = res.data.content.list;

      pagination.value.current = params.page;
      pagination.value.total = res.data.content.list;

      loading.value = false;
      resolve(res);
    })
  })
}

/**
 * 分页
 */
const pagination = ref({
  current: 1,
  pageSize: 1000,
  total: 0
});
const handleTableChange = (pagination: { current: number, pageSize: number }) => {
  handleQueryUser({
    page: pagination.current,
    size: pagination.pageSize
  });
}

onMounted(() => {
  handleQueryUser({
    page: 1,
    size: pagination.value.pageSize
  });
});

</script>