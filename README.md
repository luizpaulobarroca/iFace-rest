# iFace-rest
Rest Service for iFace

# Need:
## Database (MYSQL)
Name: iface
## Lombok
http://jnb.ociweb.com/jnb/jnbJan2010.html#installation
## Main Class
IFaceApplication

## POSTMAN
https://www.getpostman.com/
Use postman to do the resquests. Send the data as JSON.
Entitys modelled on package iFace.model

# Urls
- GET /community/all (iFace.resources.CommunityResource)
- POST /community/owner/{id} (iFace.resources.CommunityResource)
- GET     /community/{id} (iFace.resources.CommunityResource)
- POST    /community/{id} (iFace.resources.CommunityResource)
- POST    /community/{id}/add/{id2} (iFace.resources.CommunityResource)
- GET     /messages/all (iFace.resources.MessagesResource)
- GET     /messages/{id} (iFace.resources.MessagesResource)
- POST    /messages/{id}/to/{id2} (iFace.resources.MessagesResource)
- POST    /messages/{id}/toc/{id2} (iFace.resources.MessagesResource)
- POST    /user (iFace.resources.UserResource)
- GET     /user/all (iFace.resources.UserResource)
- GET     /user/{id} (iFace.resources.UserResource)
- POST    /user/{id} (iFace.resources.UserResource)
- POST    /user/{id}/accept/{id2} (iFace.resources.UserResource)
- POST    /user/{id}/add/{id2} (iFace.resources.UserResource)
