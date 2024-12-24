<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="课程名称" prop="className">
        <el-input
          v-model="queryParams.className"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable style="width: 200px">
          <el-option
            v-for="dict in training_class_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="queryTime"
          type="daterange"
          start-placeholder="Start date"
          end-placeholder="End date"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          date-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="classList"
      row-key="classId"
      :default-expand-all="isExpandAll"
    >
      <el-table-column type="index" label="序号" width="50" />
      <el-table-column
        prop="className"
        label="课程名称"
        width="140"
      ></el-table-column>
      <el-table-column
        prop="teacherName"
        label="讲师"
        width="80"
      ></el-table-column>
      <el-table-column
        prop="maxParticipantCount"
        label="报名人数"
        width="80"
        align="center"
        :formatter="countFormat"
      ></el-table-column>
      <el-table-column
        prop="location"
        label="上课地点"
        width="100"
      ></el-table-column>
      <el-table-column prop="progress" label="进度" width="80">
        <template #default="scope">
          <span>{{ scope.row.progress ? scope.row.progress : 0 }}%</span>
        </template>
      </el-table-column>
      <el-table-column prop="classBeginTime" label="课程开始时间" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.classBeginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="课程状态" width="80">
        <template #default="scope">
          <dict-tag
            :options="training_class_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column prop="signUpStatus" label="报名状态" width="80">
        <template #default="scope">
          <dict-tag
            :options="training_sign_up_status"
            :value="scope.row.signUpStatus"
          />
        </template>
      </el-table-column>
      <el-table-column prop="completeStatus" label="完成状态" width="80">
        <template #default="scope">
          <dict-tag
            :options="training_complete_status"
            :value="scope.row.completeStatus"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            >查看详情</el-button
          >
          <el-button
            v-if="
              scope.row.signUpStatus == '1' &&
              (scope.row.status == '0' || scope.row.status == '1')
            "
            link
            type="primary"
            icon="Edit"
            @click="handleCheck(scope.row)"
            >签到</el-button
          >
          <el-button
            v-if="scope.row.signUpStatus == '0'"
            link
            type="primary"
            icon="Edit"
            @click="handleSignUp(scope.row)"
            >报名</el-button
          >
          <el-button
            v-if="scope.row.signUpStatus == '1'"
            link
            style="color: red"
            type="primary"
            icon="Edit"
            @click="handleCancelSignUp(scope.row)"
            >取消报名</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 课程详情对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form
        ref="classRef"
        :model="form"
        :rules="rules"
        label-width="80px"
        disabled="true"
      >
        <el-row>
          <el-col :span="22">
            <el-form-item label="课程名称" prop="className">
              <el-input v-model="form.className" placeholder="请输入课程名称" />
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="讲师" prop="teacherName">
              <el-input
                v-model="form.teacherName"
                placeholder="请输入讲师姓名"
                maxlength="20"
              />
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="上课地点" prop="location">
              <el-input
                v-model="form.location"
                placeholder="请输入上课地点"
                maxlength="500"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最多人数" prop="maxParticipantCount">
              <el-input-number
                v-model="form.maxParticipantCount"
                :min="1"
                :max="500"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="已报人数" prop="signUpCount">
              <el-input-number v-model="form.signUpCount" :min="1" :max="500" />
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="开课时间" prop="classBeginTimeStr">
              <el-date-picker
                v-model="form.classBeginTimeStr"
                type="datetime"
                placeholder="请选择开课时间"
                style="width: 100%"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="课程内容" prop="content">
              <el-input
                v-model="form.content"
                style="width: 100%"
                :rows="8"
                type="textarea"
                placeholder="请输入课程内容"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SignUp">
import {addClass, cancelSignUp, check, delClass, getClass, listSignUp, signUp, updateClass,} from "@/api/system/class";
import useUserStore from "@/store/modules/user";

const { proxy } = getCurrentInstance();
const { training_class_status } = proxy.useDict("training_class_status");
const { training_sign_up_status } = proxy.useDict("training_sign_up_status");
const { training_complete_status } = proxy.useDict("training_complete_status");

const classList = ref([]);
const queryTime = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const isExpandAll = ref(true);
const refreshTable = ref(true);
const status = ref("");
const statusOpen = ref(false);
const currentClassId = ref(null);

const userStore = useUserStore();

const data = reactive({
  form: {},
  queryParams: {
    className: undefined,
    teacherName: undefined,
    queryBeginTime: undefined,
    queryEndTime: undefined,
    status: undefined,
    peopleId: userStore.id,
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询课程列表 */
function getList() {
  loading.value = true;
  if (queryTime.value && queryTime.value.length > 1) {
    queryParams.value.queryBeginTimeStr = queryTime.value[0];
    queryParams.value.queryEndTimeStr = queryTime.value[1];
  } else {
    queryParams.value.queryBeginTimeStr = null;
    queryParams.value.queryEndTimeStr = null;
  }
  listSignUp(queryParams.value).then((response) => {
    classList.value = response.data;
    loading.value = false;
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 取消修改状态按钮 */
function cancelStatus() {
  statusOpen.value = false;
}

/** 表单重置 */
function reset() {
  form.value = {
    classId: undefined,
    className: undefined,
    classBeginTime: undefined,
    location: undefined,
    teacherName: undefined,
    content: undefined,
    status: "0",
    maxParticipantCount: 30,
  };
  proxy.resetForm("classRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加课程";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getClass(row.classId).then((response) => {
    form.value = response.data;
    form.value.classBeginTimeStr = response.data.classBeginTime;
    open.value = true;
    title.value = "课程详情";
  });
}

/** 修改状态按钮操作 */
function handleUpdateStatus(row) {
  statusOpen.value = true;
  currentClassId.value = row.classId;
  status.value = row.status;
}

/** 修改状态 */
function changeStatus() {
  updateClass({ classId: currentClassId.value, status: status.value }).then(
    () => {
      statusOpen.value = false;
      proxy.$modal.msgSuccess("修改成功");
      open.value = false;
      getList();
    }
  );
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["classRef"].validate((valid) => {
    if (valid) {
      form.value.classBeginTime = null;
      if (form.value.classId != undefined) {
        updateClass(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addClass(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal
    .confirm('是否确认删除课程【"' + row.className + '"】?')
    .then(function () {
      return delClass(row.classId);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

/** 报名 */
function handleSignUp(row) {
  proxy.$modal
    .confirm('是否确认报名课程【"' + row.className + '"】?')
    .then(function () {
      return signUp({
        classId: row.classId,
        peopleId: userStore.id,
        peopleName: userStore.name,
      });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("报名成功");
    })
    .catch(() => {});
}

/** 签到 */
function handleCheck(row) {
  proxy.$modal
    .confirm('是否确认签到课程【"' + row.className + '"】?')
    .then(function () {
      return check({
        signUpId: row.signUpId,
      });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("签到成功");
    })
    .catch(() => {});
}

/** 取消报名 */
function handleCancelSignUp(row) {
  proxy.$modal
    .confirm('是否确认取消报名课程【"' + row.className + '"】?')
    .then(function () {
      return cancelSignUp({
        signUpId: row.signUpId,
      });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("取消报名成功");
    })
    .catch(() => {});
}

/** 人数列格式化 */
function countFormat(row, column) {
  return row.signUpCount + "/" + row.maxParticipantCount;
}

getList();
</script>
