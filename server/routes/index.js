const express = require ('express');
const contact = require ('./contact/index');
const gallery = require ('./gallery/index');
const user = require ('./user/index');

const mainRouter = express.Router()

mainRouter.use('/contact', contact)
mainRouter.use('/gallery', gallery)
mainRouter.use('/user', user)

mainRouter.get('/', (req, res) => {
  res.send("Welcome to our server!")
})

module.exports = mainRouter;
