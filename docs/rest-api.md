# Rest API for simter-operation

The following `{context-path}` could be configured by property `module.rest-context-path.simter-operation`. Its default value is `/operation`.

Provide rest APIs:

1. Create one operation.
2. Find by cluster.

## 1. Create one operation

### Request

```
POST {context-path}/
Content-Type : application/json;charset=utf-8

[
  {
    type, title, comment, result, cluster,
    operator : {id, name}, 
    target : {id, type, name}, 
    fields : [{id, name, type, oldValue, newValue}, ...],
    attachments : [{id, name, ext, size}, ...]
  },
  ...
]
```

| Name             | Type    | Require | Description
|------------------|---------|---------|-------------
| type             | String  | true    | 操作类型，如 "Creation"、"Modification"、"Deletion"
| title            | String  | false   | 操作简述，如 "创建新用户"，不提供则默认为 "${target.name} $type"
| operator.id      | String  | true    | 操作人 ID，如 "XiaoMing"
| operator.name    | String  | true    | 操作人姓名，如 "小明"
| target.id        | String  | true    | 操作对象的 ID， 如 "admin"
| target.type      | String  | true    | 操作对象的类型标识，如 "User"
| target.name      | String? | false   | 操作对象的类型名称，如 "用户"，不提供默认为 null
| comment          | String? | false   | 操作的附加说明，如当 type 为审核不通过类型时，此值可以用来记录审核意见
| result           | String? | false   | 操作的结果，如当 type 为审核不通过类型时，通过此值来区分不同的不通过方式，例如 "错别字"、"分析错误" 等
| cluster          | String? | false   | 分组，用于将某些操作归在一起进行分组，例如要将同一单事故的的报案操作、登记操作、报告操作等归为一组，可以设置 cluster=accident-{CASE_ID}
| fields           | []?     | false   | 当 type 为修改类型的操作时使用（如 modification），记录更新字段的详细信息
| fields/id        | String  | true    | 字段的标识符，如 "name"
| fields/name      | String  | false   | 字段的描述名，如 "名称"，不提供则默认为 "fields/id" 的值
| fields/type      | String  | true    | 字段的值类型，如 "String"、"Int"
| fields/oldValue  | String? | false   | 修改前的值的字符表示，不提供则默认为 null
| fields/newValue  | String? | false   | 修改后的值的字符表示，不提供则默认为 null
| attachments      | []?     | false   | 操作附带的附件
| attachments/id   | String  | true    | 附件标识符，现时等于 simter-file 文件服务器上传附件的 ID 值
| attachments/name | String  | true    | 附件名称，"半身照"
| attachments/ext  | String  | true    | 附件扩展名，如 "jpg"
| attachments/size | Number  | true    | 附件大小，单位为字节

### Response

```
204 No Content
```

## 2. Find by cluster

### Request

```
GET {context-path}/cluster/{cluster}
```

### Response

```
200 OK
Content-Type : application/json;charset=utf-8

[
  {
    id, time, type, title, comment, result, cluster,
    operator : {id, name}, 
    target : {id, type, name}, 
    fields : [{id, name, type, oldValue, newValue}, ...],
    attachments : [{id, name, ext, size}, ...]
  },
  ...
]
```

返回结果按操作时间逆序排序。

| Name             | Type    | Description
|------------------|---------|-------------
| id               | String  |
| time             | String  | 操作时间，格式为 "yyyy-MM-dd HH:mm"
| type             | String  | 操作类型，如 "Creation"、"Modification"、"Deletion"
| title            | String  | 操作简述，如 "创建新用户"
| operator.id      | String  | 操作人 ID，如 "XiaoMing"
| operator.name    | String  | 操作人姓名，如 "小明"
| target.id        | String  | 操作对象的 ID， 如 "admin"
| target.type      | String  | 操作对象的类型标识，如 "User"
| target.name      | String? | 操作对象的类型名称，如 "用户"
| comment          | String? | 操作的附加说明，如当 type 为审核不通过类型时，此值可以用来记录审核意见
| result           | String? | 操作的结果，如当 type 为审核不通过类型时，通过此值来区分不同的不通过方式，例如 "错别字"、"分析错误" 等
| cluster          | String? | 分组，用于将某些操作归在一起进行分组，例如要将同一单事故的的报案操作、登记操作、报告操作等归为一组，可以设置 cluster=accident-{CASE_ID}
| fields           | []?     | 当 type 为修改类型的操作时使用（如 modification），记录更新字段的详细信息
| fields/id        | String  | 字段的标识符，如 "name"
| fields/name      | String  | 字段的描述名，如 "名称"
| fields/type      | String  | 字段的值类型，如 "String"、"Int"
| fields/oldValue  | String? | 修改前的值的字符表示
| fields/newValue  | String? | 修改后的值的字符表示
| attachments      | []?     | 操作附带的附件
| attachments/id   | String  | 附件标识符，现时等于 simter-file 文件服务器上传附件的 ID 值
| attachments/name | String  | 附件名称，"半身照"
| attachments/ext  | String  | 附件扩展名，如 "jpg"
| attachments/size | Number  | 附件大小，单位为字节