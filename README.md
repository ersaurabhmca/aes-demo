#Encrypt
curl -X POST http://localhost:8080/api/aes/encrypt \
     -H "Content-Type: text/plain" \
     -d "Hello World"

#Decrypt
curl -X POST http://localhost:8080/api/aes/decrypt \
     -H "Content-Type: text/plain" \
     -d "<encrypted-value>"
