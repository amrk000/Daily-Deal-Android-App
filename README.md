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
#### edit to change roles permissions
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
# Random data generator code to populate database
```kotlin
        lifecycleScope.launch {
            for(day in arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")){
                for(i in 0..10){

                    val images = arrayOf(R.drawable.test_image1, R.drawable.test_image2, R.drawable.test_image3)

                    val base64Image = ImageBase64Converter.encode(ContextCompat.getDrawable(applicationContext, images.random())!!,false)

                    AppDatabase.get(applicationContext).dao().addItem(ItemData(
                        name= arrayOf("Family Meal", "Ranchit", "Beef Bomb", "Chicken Fajita", "Urban X").random(),
                        description = "description text test",
                        restaurant = arrayOf("Big Meal", "EatMore", "Grilled", "Chief Ahmed", "Fast & Food").random(),
                        image = base64Image,
                        originalPrice = DecimalFormat("#.#").format(Random.nextDouble(80.0, 150.0)).toDouble(),
                        discountPrice = DecimalFormat("#.#").format(Random.nextDouble(50.0, 120.0)).toDouble(),
                        day = day.uppercase()
                    ))

                }
            }
        }
```