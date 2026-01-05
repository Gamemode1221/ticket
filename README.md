# Ticket

Swagger UI: http://localhost:8080/swagger-ui/index.html <br/>
OpenAPI JSON: http://localhost:8080/v3/api-docs
<br/><br/><br/>
JWT Token이 정상적으로 발급되지 않는 상황이 발생함.<br/><br/>
해결 방법: 
1. Jwt Secret Key 재발급.
2. Jwt Secret Key가 올바르지 않을 경우, 확인할 수 있는 디버깅 추가. 
3. AuthService.login() 메소드에 JwsHeader 추가 및 리턴값에 header 추가
4. GlobalExceptionHandler의 UnAuthorizedException 예외 순서 변경 