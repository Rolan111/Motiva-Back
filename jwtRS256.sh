# generate private key
openssl genrsa -out private.pem 2048
# extract public key from it
openssl pkcs8 -topk8 -inform PEM -outform DER -in private.pem -out private_key.der -nocrypt

#output in DER format for JAVA
openssl rsa -in private.pem -pubout -outform DER -out public_key.der
