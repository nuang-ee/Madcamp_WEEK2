const path = require('path');
const multer = require('multer');

// Set Mongoose to Access MongoDB Server
const mongoose = require('mongoose');

// Configure mongoose

// Connect to MongoDB

const db = mongoose.connection;
db.on('error', console.error);
//connected to server
db.once('open', function() {
  console.log("Connected to mongod server");
});

mongoose.connect('mongodb://localhost:27017/image');


// Define Model
const image = require('../../models/image');

// Set The Storage Engine
const storage = multer.diskStorage({
  destination: './public/uploads/',
  filename: (req, file, callback) =>
    callback(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname))
});

// Check File Type
function checkFileType(file, cb){
  // Allowed ext
  const filetypes = /jpeg|jpg|png|gif/;
  // Check ext
  const extname = filetypes.test(path.extname(file.originalname).toLowerCase());
  // Check mime
  const mimetype = filetypes.test(file.mimetype);

  if(mimetype && extname){
    return cb(null,true);
  } else {
    cb('Error: Images Only!');
  }
}

// Init Upload
const upload = multer({
  storage: storage,
  limits:{fileSize: 5 * 1024 * 1024},
  fileFilter: function(req, file, cb){
    checkFileType(file, cb);
  }
}).single('myImage');


// Create
const newImage = new image()

exports.saveImage = (req, res) => {
  console.log(req)
  upload(req, res, (err) => {
    if(err){
      console.log(err);
    } else {
      if(req.file == undefined){
        res.send('Error: No File Selected!');
      } else {
        console.log(`Uploaded file name : ${req.file.filename}`);
        newImage.contentUrl = req.file.filename;
        //TODO : Check if this is right
        newImage.localCached = true;
        newImage.markModified("contentUrl")
        newImage.markModified("localCached")
        newImage.save(function(err) { 
          if (err) {
            console.error(err);
            return;
          }
        });
        res.send('File Uploaded!');
      }
    }
  });
};




