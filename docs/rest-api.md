# Rest API for simter-operation

The rest context path  could be configured by property `module.rest-context-path.simter-operation`.
Its default value is `/operation`. The below URL is all relative to this context path.
For example a url `/target/User/admin`, its really path should be `{context-path}/target/User/admin`.

Provide rest APIs:

| SN | Method | Url                           | Supported | Description
|----|--------|-------------------------------|:---------:|-------------
| 1  | POST   | /                             |     √    | Create one operation
| 2  | GET    | /target/$targetType/$targetId |     √    | Find all operations of specific target
| 3  | GET    | /batch/$batch                 |     √    | Find all operations of specific batch
| 4  | GET    | /$id                          |           | Get one operation

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