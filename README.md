![Repo Cover](https://github.com/user-attachments/assets/7855cd1c-cbbd-4464-ab39-e6a34669da85)
### Simple MVVM android app that built with local database using room. It allows ordering one deal per day for user. Providing admin dashboard to add, delete & update data.

## App Features:
- Ordering Meals (One Per Day)
- tracking orders history
- Admin dashboard
- Permissions & Roles Management based on permission matrix Json file
- Built using Mvvm, RoomDB, Kotlin coroutines
- Contains Implementation for:
  - DateTimeHelper to convert UTC DatTime to local with specific pattern
  - ImageBase64Converter to convert Images from/to Base64
  - TextFieldValidator to validate text fields using regex

## Video Demo:
<div align="center"><video src="https://github.com/user-attachments/assets/c4942510-bf38-41e1-b2c2-c10baa2989e9"></div>

## App Apk:
<div align="center">
<a href="https://github.com/amrk000/Daily-Deal-Android-App/releases/download/demo/Daily.Deal.App.apk"><img src="https://github.com/user-attachments/assets/351afcb2-961e-4e05-9c95-a7df7c8be071" width="250"></a>
</div>

## Database ERD:
![Daily Deal ERD](https://github.com/user-attachments/assets/6cc90555-c728-4ff1-bc78-a0c1e227bfed)

## Json Configs:
### default data are stored in project/raw as json files you can edit them
### default_permissions.json (roles and permissions data)
```json
[
    {
        "addItem": true,
        "adminDashboard": true,
        "deleteItem": true,
        "editItem": true,
        "orderItem": true,
        "role": "admin"
    },
    {
        "addItem": false,
        "adminDashboard": false,
        "deleteItem": false,
        "editItem": false,
        "orderItem": true,
        "role": "user"
    }
]
```

### default_users.json (contains default admin)
```json
[
  {
    "address": "-",
    "email": "admin@dailydeal.co",
    "id": 0,
    "name": "admin",
    "password": "Admin123456789$",
    "phone": "0",
    "role": "admin"
  }
]
```
