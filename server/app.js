const express = require ('express')

const app = express();
const bodyParser = require('body-parser');
const mongoose = require('mongoose')

app.use(bodyParser.urlencoded({ extended: true }))
app.use(bodyParser.json())

const port = process.env.PORT || 4001;

// const router = require('./routes')(app)

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

mongoose.connect('mongodb://localhost/mongodb_tutorial');

/**
 * call defined collections (Maybe 'contacts' and 'gallery')
 */
const Contact = require('./models/contacts');
