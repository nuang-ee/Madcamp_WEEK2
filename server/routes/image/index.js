const express = require('express');
const image = require('./image');

const router = express.Router()

// Public Folder
//app.use(express.static('./public'));

router.get('/', (req, res) => res.send("Welcome to Image Page!"));

router.post('/get', image.getImage);
router.post('/add', image.addImage);
router.delete('/delete', image.deleteImage);

//fetch image(single)
//router.get(

module.exports = router;
