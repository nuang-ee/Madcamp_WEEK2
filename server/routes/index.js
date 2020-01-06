const express = require ('express');
const contact = require ('./contact/index');
const image = require ('./image/index');
const user = require ('./user/index');
const custom = require ('./custom/index');

const mainRouter = express.Router()

mainRouter.use('/contact', contact)
mainRouter.use('/image', image)
mainRouter.use('/user', user)
mainRouter.use('/custom', custom)

mainRouter.get('/', (req, res) => {
  res.send("Welcome to our server!")
})

module.exports = mainRouter;
