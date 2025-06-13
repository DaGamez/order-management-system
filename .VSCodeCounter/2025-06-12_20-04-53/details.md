# Details

Date : 2025-06-12 20:04:53

Directory c:\\Proyectos\\MISO\\2025 13 Modernizacion\\sample-sprint-boot

Total : 42 files,  3894 codes, 124 comments, 593 blanks, all 4611 lines

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)

## Files
| filename | language | code | comment | blank | total |
| :--- | :--- | ---: | ---: | ---: | ---: |
| [API\_DOCUMENTATION.md](/API_DOCUMENTATION.md) | Markdown | 258 | 0 | 104 | 362 |
| [README.md](/README.md) | Markdown | 133 | 0 | 36 | 169 |
| [logs/application.log](/logs/application.log) | Log | 1,028 | 0 | 17 | 1,045 |
| [logs/database.log](/logs/database.log) | Log | 298 | 0 | 1 | 299 |
| [pom.xml](/pom.xml) | XML | 74 | 0 | 5 | 79 |
| [run.bat](/run.bat) | Batch | 33 | 1 | 6 | 40 |
| [run.sh](/run.sh) | Shell Script | 28 | 2 | 6 | 36 |
| [src/main/java/com/example/crud/CrudApplication.java](/src/main/java/com/example/crud/CrudApplication.java) | Java | 9 | 0 | 4 | 13 |
| [src/main/java/com/example/crud/config/DataInitializer.java](/src/main/java/com/example/crud/config/DataInitializer.java) | Java | 57 | 5 | 16 | 78 |
| [src/main/java/com/example/crud/config/LogDirectoryConfig.java](/src/main/java/com/example/crud/config/LogDirectoryConfig.java) | Java | 39 | 2 | 7 | 48 |
| [src/main/java/com/example/crud/config/SecurityConfig.java](/src/main/java/com/example/crud/config/SecurityConfig.java) | Java | 46 | 0 | 8 | 54 |
| [src/main/java/com/example/crud/controller/AdminController.java](/src/main/java/com/example/crud/controller/AdminController.java) | Java | 12 | 3 | 4 | 19 |
| [src/main/java/com/example/crud/controller/LogController.java](/src/main/java/com/example/crud/controller/LogController.java) | Java | 58 | 6 | 12 | 76 |
| [src/main/java/com/example/crud/controller/LoginController.java](/src/main/java/com/example/crud/controller/LoginController.java) | Java | 10 | 0 | 4 | 14 |
| [src/main/java/com/example/crud/controller/ProductController.java](/src/main/java/com/example/crud/controller/ProductController.java) | Java | 79 | 8 | 14 | 101 |
| [src/main/java/com/example/crud/controller/UserController.java](/src/main/java/com/example/crud/controller/UserController.java) | Java | 89 | 3 | 15 | 107 |
| [src/main/java/com/example/crud/controller/WebController.java](/src/main/java/com/example/crud/controller/WebController.java) | Java | 85 | 0 | 16 | 101 |
| [src/main/java/com/example/crud/exception/GlobalExceptionHandler.java](/src/main/java/com/example/crud/exception/GlobalExceptionHandler.java) | Java | 63 | 1 | 14 | 78 |
| [src/main/java/com/example/crud/model/Product.java](/src/main/java/com/example/crud/model/Product.java) | Java | 74 | 3 | 22 | 99 |
| [src/main/java/com/example/crud/model/User.java](/src/main/java/com/example/crud/model/User.java) | Java | 75 | 2 | 22 | 99 |
| [src/main/java/com/example/crud/repository/ProductRepository.java](/src/main/java/com/example/crud/repository/ProductRepository.java) | Java | 11 | 3 | 6 | 20 |
| [src/main/java/com/example/crud/repository/UserRepository.java](/src/main/java/com/example/crud/repository/UserRepository.java) | Java | 10 | 0 | 6 | 16 |
| [src/main/java/com/example/crud/service/CustomUserDetailsService.java](/src/main/java/com/example/crud/service/CustomUserDetailsService.java) | Java | 34 | 0 | 8 | 42 |
| [src/main/java/com/example/crud/service/LogService.java](/src/main/java/com/example/crud/service/LogService.java) | Java | 80 | 25 | 14 | 119 |
| [src/main/java/com/example/crud/service/ProductService.java](/src/main/java/com/example/crud/service/ProductService.java) | Java | 14 | 0 | 11 | 25 |
| [src/main/java/com/example/crud/service/ProductServiceImpl.java](/src/main/java/com/example/crud/service/ProductServiceImpl.java) | Java | 55 | 0 | 17 | 72 |
| [src/main/java/com/example/crud/service/UserService.java](/src/main/java/com/example/crud/service/UserService.java) | Java | 13 | 0 | 11 | 24 |
| [src/main/java/com/example/crud/service/UserServiceImpl.java](/src/main/java/com/example/crud/service/UserServiceImpl.java) | Java | 59 | 2 | 17 | 78 |
| [src/main/resources/application.properties](/src/main/resources/application.properties) | Properties | 15 | 2 | 3 | 20 |
| [src/main/resources/logback.xml](/src/main/resources/logback.xml) | XML | 48 | 3 | 6 | 57 |
| [src/main/resources/static/css/style.css](/src/main/resources/static/css/style.css) | PostCSS | 217 | 11 | 43 | 271 |
| [src/main/resources/static/js/script.js](/src/main/resources/static/js/script.js) | JavaScript | 29 | 4 | 3 | 36 |
| [src/main/resources/templates/admin/logs.html](/src/main/resources/templates/admin/logs.html) | HTML | 175 | 0 | 10 | 185 |
| [src/main/resources/templates/error.html](/src/main/resources/templates/error.html) | HTML | 38 | 0 | 7 | 45 |
| [src/main/resources/templates/login.html](/src/main/resources/templates/login.html) | HTML | 68 | 0 | 9 | 77 |
| [src/main/resources/templates/products/form.html](/src/main/resources/templates/products/form.html) | HTML | 54 | 0 | 7 | 61 |
| [src/main/resources/templates/products/list.html](/src/main/resources/templates/products/list.html) | HTML | 77 | 4 | 6 | 87 |
| [src/main/resources/templates/users/form.html](/src/main/resources/templates/users/form.html) | HTML | 68 | 0 | 8 | 76 |
| [src/main/resources/templates/users/list.html](/src/main/resources/templates/users/list.html) | HTML | 69 | 0 | 4 | 73 |
| [src/test/java/com/example/crud/CrudApplicationTests.java](/src/test/java/com/example/crud/CrudApplicationTests.java) | Java | 9 | 0 | 4 | 13 |
| [src/test/java/com/example/crud/controller/ProductControllerTest.java](/src/test/java/com/example/crud/controller/ProductControllerTest.java) | Java | 90 | 12 | 26 | 128 |
| [src/test/java/com/example/crud/service/UserServiceTest.java](/src/test/java/com/example/crud/service/UserServiceTest.java) | Java | 113 | 22 | 34 | 169 |

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)