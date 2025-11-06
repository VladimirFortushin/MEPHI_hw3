1. The app listens to POST in a container servlet (not specified in pom.xml)
2. Request:
   POST .../link/create
  {
   "user":{
   "user_name":"some_name",
   "user_id":"uuid"
   },
   "url":"some_url"
  }
4. Response:
   {
    "link":"clck.ru/Hfos#wq",
    "expires_at":"2025-05-05T09:24:38.231666700+03:00",
    "usages":5,
    "response_code":0,
    "response_message":"Успешно"
   } 
