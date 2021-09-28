# Coffee Order App
> 커피 주문 관리 웹사이트

<br>

## 요구사항

- 고객은 웹사이트에서 다양한 커피 종류를 선택해 주문을 생성할 수 있다.
- 주문은 회원단위가 아닌 email 단위로 생성된다.
- 관리자는 상품을 생성, 조회, 수정, 삭제할 수 있다
- 관리자는 주문을 조회, 수정, 삭제할 수 있다.
- 주문은 전날 오후 2시부터 당일 오후 2시까지의 주문을 모아 처리한다.

<br>

## 실행환경

### Backend

- Java 16
- Spring Boot 2.5.4
- Maven 4
- JDBC Template
- Junit 5
- MySQL 8 (on Docker)

### Frontend

- Thymeleaf
- React
- Axios

<br>

## 프로젝트 구조

### Architecture

![클론코딩 drawio (1)](https://user-images.githubusercontent.com/71180414/134816800-c7f15403-5621-47b6-9c4d-c98140ff1dc6.png)

### Package

<image width="250" src="https://user-images.githubusercontent.com/71180414/134816512-8650c15a-1531-4ae8-ba5e-b2745a443fe1.png">

### Schema

<image width="600" src="https://user-images.githubusercontent.com/71180414/134815142-26356f46-fffb-4195-89e3-9278e3afe665.png">

<br>

##  Rest API

### 상품 목록 조회

> Request
```http request
GET http://localhost:8080/api/v1/products
```

> Response
```json
[
   { 
       "productId": "", 
       "productName": "",
       "category": "",
       "description": "",
       "createdAt": "",
       "updatedAt": ""
   },
] 
```

### 커피 주문

> Request
```http request
POST http://localhost:8080/api/v1/orders
```

```json
{
  "orderId": "",
  "email": {
    "address": ""
  },
  "address": "",
  "postcode": "",
  "orderItems": [
    {
      "productId": "",
      "category": "",
      "price": "",
      "quantity": ""
    }
  ],
  "orderStatus": "",
  "createdAt": "",
  "updatedAt": ""
}
```

<br>

## 보완할 점

- 고객의 주문 조회 기능
- 관리자의 상품 삭제 기능
- 관리자가 고객의 주문을 처리하는 기능(조회, 수정, 삭제)
- 오후 2시마다 주문의 상태를 변경하는 기능

<br>