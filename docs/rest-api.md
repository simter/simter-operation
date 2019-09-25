# Rest API for simter-operation

The rest context path  could be configured by property `module.rest-context-path.simter-operation`.
Its default value is `/operation`. The below URL is all relative to this context path.
For example a url `/target/User/admin`, its really path should be `{context-path}/target/User/admin`.

Provide rest APIs:

| SN | Method | Url                           | Supported | Description
|----|--------|-------------------------------|:---------:|-------------
| 1  | POST   | /                             |     √     | Create one operation
| 2  | GET    | /target/$targetType/$targetId |     √     | Find all operations of specific target
| 3  | GET    | /batch/$batch                 |     √     | Find all operations of specific batch
| 4  | GET    | /$id                          |     √     | Get one operation
| 5  | GET    | /                             |     √     | Find pageable operations
| 6  | GET    | /target-type                  |     √     | Find all target types

## 1. Create one operation

### Request

```
POST /
Content-Type : application/json

$operation
```

`$operation`'s json structure:

| Name         | Type    | Require | Description
|--------------|---------|---------|-------------
| type         | String  | true    | operation type, such as "Creation"、"Modification"、"Deletion"
| operatorId   | String  | true    | the operator's identifier
| operatorName | String  | true    | the operator's name, such as "John"
| targetType   | String  | true    | the target's type, such as "User"
| targetId     | String  | true    | the target's identifier, such as "admin"
| title        | String? | false   | a simple subject, such as "Create new user"
| remark       | String? | false   | detail description for this operation
| result       | String? | false   | the operation result, such as "passed", "rejected"
| batch        | String? | false   | a batch identifier for grouped a couple of operations
| items        | []?     | false   | a couple of detail item of this operation, mostly use to record form fields changed
| ├ id         | String  | true    | the item's identifier, such as "code"
| ├ title      | String? | false   | the item's simple subject, such as "bill number"
| ├ valueType  | String  | true    | the value type, such as "String", "JsonObject", "JsonArray"
| ├ oldValue   | String? | false   | old value
| ├ newValue   | String? | false   | new value

### Response

```
204 No Content
```

## 2. Find by target type and id

Find all operations of specific target type and id. Order by `Operation.ts` descendant.

### Request

```
GET /target/$targetType/$targetId
```

### Response

```
200 OK
Content-Type : application/json

[$operation, ...]
```

`$operation` 's json structure: it is the same with `$operation` above in「1. Create one operation」,
but add more below:

| Name             | Type    | Require | Description
|------------------|---------|---------|-------------
| id               | String  | true    | operation's identifier
| ts               | String  | true    | the operated date-time, ISO format such as `2019-01-01T08:30:10+08:00`

If found nothing, response:

```
204 No Content
```

## 3. Find by batch

Find all operations of specific batch. Order by `Operation.ts` descendant.

### Request

```
GET /batch/$batch
```

### Response

```
200 OK
Content-Type : application/json

[$operation, ...]
```


`$operation` 's json structure: it is the same with `$operation` above in「1. Create one operation」.

If found nothing, response:

```
204 No Content
```

## 4. Get one operation

Get one operation of specific id.

### Request

```
GET /$id
```

### Response

```
200 OK
Content-Type : application/json

$operation
```


`$operation` 's json structure: it is the same with `$operation` above in「1. Create one operation」.

If not exists, response:

```
404 No Found
```

## 5. Find pageable operations

**Request**

```
GET /?batch=x?target-type=x?target-id=x&search=x&page-no=x&page-size=x
```

| Name        | Require | Description
|-------------|---------|-------------
| batch       | false   | the batch, support multiple values, such as `batch=b1&batch=b2`
| target-type | false   | the target's type, support multiple values, such as `target-type=t1&target-type=t2`
| target-id   | false   | the target's id, support multiple values, such as `target-id=1&target-id=2`
| search      | false   | fuzzy search value, match with title, operatorName, batch and targetType
| page-no     | false   | page number, default 1
| page-size   | false   | page size, default 25

**Response**

```
200 OK
Content-Type : application/json

{ count, pageNo, pageSize, rows: [{ROW}, ...] }
```

> `rows` is sorted by operationTime desc.

`{ROW}` structure:

| Name          | Type    | Description
|---------------|---------|-------------
| id            | String  | operation id
| type          | String  | operation type, such as "Creation"、"Modification"、"Deletion"
| ts            | String  | operation time, ISO format such as `2019-01-01T08:30:10+08:00`
| title         | String  | operation title
| operatorName  | String  | operator name
| targetType    | String  | the target's type, such as "User"
| targetId      | String  | the target's id, such as "1"
| batch         | String  | the batch, such as "Organization"

if do not have read permission then response return:  

```
403 Forbidden
Content-Type : plain/text

Permission denied!
```

## 6. Find all target types

**Request**

```
GET /target-type
```

**Response**

```
200 OK
Content-Type : application/json

["RepairOrder", "RepairItem", ...]
```
> Sort by target type asc.