const express = require ('express');
const contact = require ('./contact/index');
const image = require ('./image/index');
const user = require ('./user/index');
const claimer = require ('./claimer/index');
const claimee = require ('./claimee/index');

const mainRouter = express.Router()

mainRouter.use('/contact', contact)
mainRouter.use('/image', image)
mainRouter.use('/user', user)
mainRouter.use('/claimer', claimer)
mainRouter.use('/claimee', claimee)

mainRouter.get('/', (req, res) => {
  res.send("Welcome to our server!")
})

module.exports = mainRouter;
