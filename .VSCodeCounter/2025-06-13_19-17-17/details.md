# Details

Date : 2025-06-13 19:17:17

Directory c:\\Proyectos\\MISO\\2025 13 Modernizacion\\order-management-system

Total : 61 files,  31581 codes, 205 comments, 881 blanks, all 32667 lines

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)

## Files
| filename | language | code | comment | blank | total |
| :--- | :--- | ---: | ---: | ---: | ---: |
| [API\_DOCUMENTATION.md](/API_DOCUMENTATION.md) | Markdown | 258 | 0 | 104 | 362 |
| [README.md](/README.md) | Markdown | 132 | 0 | 36 | 168 |
| [logs/archived/application.2025-06-12.log](/logs/archived/application.2025-06-12.log) | Log | 10,105 | 0 | 34 | 10,139 |
| [logs/archived/database.2025-06-12.log](/logs/archived/database.2025-06-12.log) | Log | 17,391 | 0 | 11 | 17,402 |
| [pom.xml](/pom.xml) | XML | 77 | 0 | 5 | 82 |
| [run.bat](/run.bat) | Batch | 33 | 1 | 6 | 40 |
| [run.sh](/run.sh) | Shell Script | 28 | 2 | 6 | 36 |
| [src/main/java/com/example/crud/OrderManagementSystemApplication.java](/src/main/java/com/example/crud/OrderManagementSystemApplication.java) | Java | 9 | 0 | 4 | 13 |
| [src/main/java/com/example/crud/aspect/UserQueryAspect.java](/src/main/java/com/example/crud/aspect/UserQueryAspect.java) | Java | 114 | 11 | 21 | 146 |
| [src/main/java/com/example/crud/config/AspectConfig.java](/src/main/java/com/example/crud/config/AspectConfig.java) | Java | 7 | 1 | 3 | 11 |
| [src/main/java/com/example/crud/config/DataInitializer.java](/src/main/java/com/example/crud/config/DataInitializer.java) | Java | 80 | 8 | 22 | 110 |
| [src/main/java/com/example/crud/config/SecurityConfig.java](/src/main/java/com/example/crud/config/SecurityConfig.java) | Java | 46 | 0 | 7 | 53 |
| [src/main/java/com/example/crud/config/WebConfig.java](/src/main/java/com/example/crud/config/WebConfig.java) | Java | 27 | 3 | 4 | 34 |
| [src/main/java/com/example/crud/controller/DashboardController.java](/src/main/java/com/example/crud/controller/DashboardController.java) | Java | 55 | 5 | 14 | 74 |
| [src/main/java/com/example/crud/controller/LogController.java](/src/main/java/com/example/crud/controller/LogController.java) | Java | 58 | 6 | 12 | 76 |
| [src/main/java/com/example/crud/controller/LoginController.java](/src/main/java/com/example/crud/controller/LoginController.java) | Java | 10 | 0 | 4 | 14 |
| [src/main/java/com/example/crud/controller/OrderController.java](/src/main/java/com/example/crud/controller/OrderController.java) | Java | 121 | 6 | 19 | 146 |
| [src/main/java/com/example/crud/controller/OrderWebController.java](/src/main/java/com/example/crud/controller/OrderWebController.java) | Java | 108 | 0 | 26 | 134 |
| [src/main/java/com/example/crud/controller/ProductController.java](/src/main/java/com/example/crud/controller/ProductController.java) | Java | 80 | 7 | 13 | 100 |
| [src/main/java/com/example/crud/controller/UserController.java](/src/main/java/com/example/crud/controller/UserController.java) | Java | 89 | 3 | 15 | 107 |
| [src/main/java/com/example/crud/controller/WebController.java](/src/main/java/com/example/crud/controller/WebController.java) | Java | 85 | 0 | 16 | 101 |
| [src/main/java/com/example/crud/exception/GlobalExceptionHandler.java](/src/main/java/com/example/crud/exception/GlobalExceptionHandler.java) | Java | 63 | 1 | 14 | 78 |
| [src/main/java/com/example/crud/model/Order.java](/src/main/java/com/example/crud/model/Order.java) | Java | 99 | 2 | 26 | 127 |
| [src/main/java/com/example/crud/model/Product.java](/src/main/java/com/example/crud/model/Product.java) | Java | 74 | 3 | 22 | 99 |
| [src/main/java/com/example/crud/model/User.java](/src/main/java/com/example/crud/model/User.java) | Java | 75 | 2 | 22 | 99 |
| [src/main/java/com/example/crud/model/UserQuery.java](/src/main/java/com/example/crud/model/UserQuery.java) | Java | 64 | 3 | 21 | 88 |
| [src/main/java/com/example/crud/repository/OrderRepository.java](/src/main/java/com/example/crud/repository/OrderRepository.java) | Java | 11 | 0 | 4 | 15 |
| [src/main/java/com/example/crud/repository/ProductRepository.java](/src/main/java/com/example/crud/repository/ProductRepository.java) | Java | 11 | 3 | 6 | 20 |
| [src/main/java/com/example/crud/repository/UserQueryRepository.java](/src/main/java/com/example/crud/repository/UserQueryRepository.java) | Java | 22 | 5 | 10 | 37 |
| [src/main/java/com/example/crud/repository/UserRepository.java](/src/main/java/com/example/crud/repository/UserRepository.java) | Java | 10 | 0 | 6 | 16 |
| [src/main/java/com/example/crud/service/CustomUserDetailsService.java](/src/main/java/com/example/crud/service/CustomUserDetailsService.java) | Java | 34 | 0 | 8 | 42 |
| [src/main/java/com/example/crud/service/LogService.java](/src/main/java/com/example/crud/service/LogService.java) | Java | 113 | 34 | 15 | 162 |
| [src/main/java/com/example/crud/service/OrderService.java](/src/main/java/com/example/crud/service/OrderService.java) | Java | 13 | 0 | 4 | 17 |
| [src/main/java/com/example/crud/service/OrderServiceImpl.java](/src/main/java/com/example/crud/service/OrderServiceImpl.java) | Java | 53 | 0 | 12 | 65 |
| [src/main/java/com/example/crud/service/ProductService.java](/src/main/java/com/example/crud/service/ProductService.java) | Java | 14 | 0 | 11 | 25 |
| [src/main/java/com/example/crud/service/ProductServiceImpl.java](/src/main/java/com/example/crud/service/ProductServiceImpl.java) | Java | 55 | 0 | 17 | 72 |
| [src/main/java/com/example/crud/service/UserQueryService.java](/src/main/java/com/example/crud/service/UserQueryService.java) | Java | 65 | 24 | 21 | 110 |
| [src/main/java/com/example/crud/service/UserService.java](/src/main/java/com/example/crud/service/UserService.java) | Java | 13 | 0 | 11 | 24 |
| [src/main/java/com/example/crud/service/UserServiceImpl.java](/src/main/java/com/example/crud/service/UserServiceImpl.java) | Java | 59 | 2 | 17 | 78 |
| [src/main/resources/application.properties](/src/main/resources/application.properties) | Properties | 15 | 2 | 3 | 20 |
| [src/main/resources/db/migration/V2\_\_Add\_User\_Queries\_Table.sql](/src/main/resources/db/migration/V2__Add_User_Queries_Table.sql) | MS SQL | 10 | 3 | 3 | 16 |
| [src/main/resources/logback.xml](/src/main/resources/logback.xml) | XML | 48 | 3 | 6 | 57 |
| [src/main/resources/static/css/orders.css](/src/main/resources/static/css/orders.css) | PostCSS | 24 | 2 | 5 | 31 |
| [src/main/resources/static/css/style.css](/src/main/resources/static/css/style.css) | PostCSS | 224 | 12 | 44 | 280 |
| [src/main/resources/static/js/orderForm.js](/src/main/resources/static/js/orderForm.js) | JavaScript | 22 | 5 | 4 | 31 |
| [src/main/resources/static/js/script.js](/src/main/resources/static/js/script.js) | JavaScript | 29 | 4 | 3 | 36 |
| [src/main/resources/templates/admin/dashboard.html](/src/main/resources/templates/admin/dashboard.html) | HTML | 353 | 1 | 36 | 390 |
| [src/main/resources/templates/admin/logs.html](/src/main/resources/templates/admin/logs.html) | HTML | 204 | 0 | 12 | 216 |
| [src/main/resources/templates/error.html](/src/main/resources/templates/error.html) | HTML | 37 | 0 | 7 | 44 |
| [src/main/resources/templates/login.html](/src/main/resources/templates/login.html) | HTML | 68 | 0 | 9 | 77 |
| [src/main/resources/templates/orders/form.html](/src/main/resources/templates/orders/form.html) | HTML | 73 | 0 | 6 | 79 |
| [src/main/resources/templates/orders/list.html](/src/main/resources/templates/orders/list.html) | HTML | 83 | 3 | 6 | 92 |
| [src/main/resources/templates/products/form.html](/src/main/resources/templates/products/form.html) | HTML | 61 | 0 | 7 | 68 |
| [src/main/resources/templates/products/list.html](/src/main/resources/templates/products/list.html) | HTML | 81 | 4 | 6 | 91 |
| [src/main/resources/templates/users/form.html](/src/main/resources/templates/users/form.html) | HTML | 91 | 0 | 10 | 101 |
| [src/main/resources/templates/users/list.html](/src/main/resources/templates/users/list.html) | HTML | 95 | 0 | 5 | 100 |
| [src/test/java/com/example/crud/OrderManagementSystemApplicationTests.java](/src/test/java/com/example/crud/OrderManagementSystemApplicationTests.java) | Java | 9 | 0 | 4 | 13 |
| [src/test/java/com/example/crud/config/TestSecurityConfig.java](/src/test/java/com/example/crud/config/TestSecurityConfig.java) | Java | 51 | 0 | 9 | 60 |
| [src/test/java/com/example/crud/config/TestUserDetailsConfig.java](/src/test/java/com/example/crud/config/TestUserDetailsConfig.java) | Java | 29 | 0 | 7 | 36 |
| [src/test/java/com/example/crud/controller/ProductControllerTest.java](/src/test/java/com/example/crud/controller/ProductControllerTest.java) | Java | 100 | 12 | 26 | 138 |
| [src/test/java/com/example/crud/service/UserServiceTest.java](/src/test/java/com/example/crud/service/UserServiceTest.java) | Java | 113 | 22 | 34 | 169 |

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)