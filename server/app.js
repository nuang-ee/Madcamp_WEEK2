const express = require ('express')

const app = express();
const bodyParser = require('body-parser');
const mongoose = require('mongoose')

/**
 * body-parser configuration
 */
app.use(bodyParser.urlencoded({ extended: true }))
app.use(bodyParser.json())

/**
 * set port number (default === 4001)
 */
const port = process.env.PORT || 4002;

const router = require('./routes/index');
app.use('/', router);

/**
 * run server
 */

/*
app.get('/', (req, res) => {
    res.send("Successfully accessed to server!")
})
*/


const server = app.listen(port, () => {
    console.log("Express server has started on port " + port)
})

/**
 * Connect to MongoDB
 */
const mongodb = mongoose.connection;
mongodb.on('error', console.error);
mongodb.once('open', () => {
    console.log("Connected to mongodb server");
});

const url = 'mongodb://127.0.0.1:27017'
mongoose.connect(url, {
    useNewUrlParser: true,
    useUnifiedTopology: true
  }, (err, client) => {
  if (err) {
    console.error(err)
    return;
  }
})
