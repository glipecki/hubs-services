for c in {1..100000}; do curl -X POST http://localhost:8080/v1/service/EchoService -H "Content-Type: application/json" -H "Accept: application/json" -d 'dupeczka ja jaja ja! :)' 2&> /dev/null; done
