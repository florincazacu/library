# REST API library

This is a simple library providing REST API as described below

**AUTHENTICATION**
----

* **URL**

  /login

* **Method**

  `POST`

* **Data Params**

`username = STRING`
`password = STRING`

* **Sample Call:**

  ```javascript
  {
  "username": "user",
  "password": "password"
  }
  ```

* **Success Response:**

    * **Code:** 200 OK <br />

* **Error Response:**

    * **Code:** 401 UNAUTHORIZED <br />

<br />

* **URL**

  /register

* **Method:**

  `POST`

* **Data Params**

`username = STRING`
`password = STRING`
`email = STRING`

* **Sample Call:**

  ```javascript
  {
  "username": "user",
  "password": "password",
  "email": user@domain.com
  }
  ```

* **Success Response:**

    * **Code:** 201 CREATED <br />

* **Error Response:**

    * **Code:** 400 BAD_REQUEST <br />
*  **Validation: email**



**BOOK**
----

* **URL**

  /books

* **Method**

  `GET`

* **Data Params**

`username = STRING`
`password = STRING`

* **Sample Call:**

  ```javascript
  {
  "username": "user",
  "password": "password"
  }
  ```

* **Success Response:**

  * **Code:** 200 OK <br />
  * **Content**

    ```javascript
    {
        "id": 2,
        "title": "Clean Code",
        "copies": 3,
        "stock": 2,
        "authors": [
          {
            "id": 2,
            "firstName": "Robert",
            "lastName": "Martin"
          }
        ],
        "categories": [
          {
            "id": 1,
            "name": "Programming"
          }
        ],
        "loan": {
          "id": 1,
          "loanDate": "19-01-2022",
          "returnDate": null
        }
      }
    ```

* **Error Response:**

  * **Code:** 401 NOT_FOUND <br />
  * **Code:** 401 UNAUTHORIZED <br />
