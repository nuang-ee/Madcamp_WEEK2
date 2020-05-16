# Madcamp_WEEK2

**This Project is deprecated. Should be updated properly to be used.**

##### For developing : 
After cloning, run the code below:
```
cd server
npm install
```

This is the resulting Android application, which is developed within week 2 of KAIST Madcamp.  

The goal is to develop a app with 3 tabs with different functionalities.  

Difference between the project 1 of Madcamp is, this week's app is mainly about using server & DB.  

We took nodejs + MongoDB to develop the entire app & REST API server.


### The initialization of the app.
- This app needs permission to access internet, local contacts, and User gallery.  
So at the very first launch of this app, the permissions mentioned above is required to the user.  
- This app uses Facebook Login API to authenticate users. After login, user can save their own  
informations on the server, and download & modify them too.

### The functionalities of each tabs
#### Tab 1 : Contacts
- Fetches user's local device's contacts, and shows the name, and phone number, email address.  
- Used SwipeRefresh feature to refresh the lists of contacts in a simple, intuitive way.  
- The local device's contacts is uploaded to server automatically.  
- Implemented "swipe to delete/edit" feature, which makes the modification of the list of contacts more   
intuitive and fast.

#### Tab 2 : Gallery
- Shows photos saved in server, in a 3xN tile views.  
- Photos are saved as byteArray in server, and also saved in mongoDB as a url.  
- SwipeRefresh to refresh the lists of photos, to be synchronized with the device.  
- We can Upload photos from local device to server, and delete from server with gestures.  
It is available to handle multiple photos at once.  

#### Tab 3 : Credit & Debt Viewer.
- User can register the credit & debt history between other users, with the informations of that history,  
with another user's nickname.
- Debtor can request to unregister the history after paying back, then Creditor can check the validity of  
request, and finally accept to unregister the history.
- All the informations about credits & debts are saved in the server.
