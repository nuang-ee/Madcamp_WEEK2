const path = require('path');
const multer = require('multer');

// Set The Storage Engine
const storage = multer.diskStorage({
    destination: './public/uploads/',
    filename: (req, file, callback) =>
        callback(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname))
});

// Check File Type
function checkFileType(file, cb) {
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
exports.upload = multer({
    storage: storage,
    limits:{ fileSize: 5 * 1024 * 1024 }, // set max file size to 5MB
    fileFilter: function(req, file, cb) {
        checkFileType(file, cb);
    }
}).single('myImage');
