# Details

Date : 2025-06-12 13:42:41

Directory c:\\Proyectos\\MISO\\2025 13 Modernizacion\\sample-sprint-boot

Total : 47 files,  2819 codes, 104 comments, 600 blanks, all 3523 lines

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)

## Files
| filename | language | code | comment | blank | total |
| :--- | :--- | ---: | ---: | ---: | ---: |
| [API\_DOCUMENTATION.md](/API_DOCUMENTATION.md) | Markdown | 258 | 0 | 104 | 362 |
| [README.md](/README.md) | Markdown | 133 | 0 | 36 | 169 |
| [logs/application.log](/logs/application.log) | Log | 94 | 0 | 1 | 95 |
| [pom.xml](/pom.xml) | XML | 74 | 0 | 5 | 79 |
| [run.bat](/run.bat) | Batch | 33 | 1 | 6 | 40 |
| [run.sh](/run.sh) | Shell Script | 28 | 2 | 6 | 36 |
| [src/main/java/com/example/crud/CrudApplication.java](/src/main/java/com/example/crud/CrudApplication.java) | Java | 9 | 0 | 4 | 13 |
| [src/main/java/com/example/crud/config/DataInitializer.java](/src/main/java/com/example/crud/config/DataInitializer.java) | Java | 57 | 5 | 16 | 78 |
| [src/main/java/com/example/crud/config/SecurityConfig.java](/src/main/java/com/example/crud/config/SecurityConfig.java) | Java | 45 | 0 | 8 | 53 |
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
| [src/main/java/com/example/crud/service/ProductService.java](/src/main/java/com/example/crud/service/ProductService.java) | Java | 14 | 0 | 11 | 25 |
| [src/main/java/com/example/crud/service/ProductServiceImpl.java](/src/main/java/com/example/crud/service/ProductServiceImpl.java) | Java | 55 | 0 | 17 | 72 |
| [src/main/java/com/example/crud/service/UserService.java](/src/main/java/com/example/crud/service/UserService.java) | Java | 13 | 0 | 11 | 24 |
| [src/main/java/com/example/crud/service/UserServiceImpl.java](/src/main/java/com/example/crud/service/UserServiceImpl.java) | Java | 59 | 2 | 17 | 78 |
| [src/main/resources/application.properties](/src/main/resources/application.properties) | Properties | 12 | 2 | 3 | 17 |
| [src/main/resources/logback.xml](/src/main/resources/logback.xml) | XML | 26 | 0 | 4 | 30 |
| [src/main/resources/static/css/style.css](/src/main/resources/static/css/style.css) | PostCSS | 162 | 10 | 32 | 204 |
| [src/main/resources/static/js/script.js](/src/main/resources/static/js/script.js) | JavaScript | 29 | 4 | 3 | 36 |
| [src/main/resources/templates/error.html](/src/main/resources/templates/error.html) | HTML | 38 | 0 | 7 | 45 |
| [src/main/resources/templates/login.html](/src/main/resources/templates/login.html) | HTML | 68 | 0 | 9 | 77 |
| [src/main/resources/templates/products/form.html](/src/main/resources/templates/products/form.html) | HTML | 54 | 0 | 7 | 61 |
| [src/main/resources/templates/products/list.html](/src/main/resources/templates/products/list.html) | HTML | 75 | 4 | 6 | 85 |
| [src/main/resources/templates/users/form.html](/src/main/resources/templates/users/form.html) | HTML | 68 | 0 | 8 | 76 |
| [src/main/resources/templates/users/list.html](/src/main/resources/templates/users/list.html) | HTML | 69 | 0 | 4 | 73 |
| [src/test/java/com/example/crud/CrudApplicationTests.java](/src/test/java/com/example/crud/CrudApplicationTests.java) | Java | 9 | 0 | 4 | 13 |
| [src/test/java/com/example/crud/controller/ProductControllerTest.java](/src/test/java/com/example/crud/controller/ProductControllerTest.java) | Java | 90 | 12 | 26 | 128 |
| [src/test/java/com/example/crud/service/UserServiceTest.java](/src/test/java/com/example/crud/service/UserServiceTest.java) | Java | 113 | 22 | 34 | 169 |
| [target/classes/application.properties](/target/classes/application.properties) | Properties | 12 | 2 | 3 | 17 |
| [target/classes/logback.xml](/target/classes/logback.xml) | XML | 26 | 0 | 4 | 30 |
| [target/classes/static/css/style.css](/target/classes/static/css/style.css) | PostCSS | 162 | 10 | 32 | 204 |
| [target/classes/static/js/script.js](/target/classes/static/js/script.js) | JavaScript | 29 | 4 | 3 | 36 |
| [target/classes/templates/error.html](/target/classes/templates/error.html) | HTML | 38 | 0 | 7 | 45 |
| [target/classes/templates/login.html](/target/classes/templates/login.html) | HTML | 68 | 0 | 9 | 77 |
| [target/classes/templates/products/form.html](/target/classes/templates/products/form.html) | HTML | 54 | 0 | 7 | 61 |
| [target/classes/templates/products/list.html](/target/classes/templates/products/list.html) | HTML | 75 | 4 | 6 | 85 |
| [target/classes/templates/users/form.html](/target/classes/templates/users/form.html) | HTML | 68 | 0 | 8 | 76 |
| [target/classes/templates/users/list.html](/target/classes/templates/users/list.html) | HTML | 69 | 0 | 4 | 73 |
| [target/maven-archiver/pom.properties](/target/maven-archiver/pom.properties) | Properties | 3 | 0 | 1 | 4 |

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)