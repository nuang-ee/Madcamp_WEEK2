const mongoose = require('mongoose');
const imageSchema = new mongoose.Schema(
  { 
    _id: Number,
    img: {
      data: Buffer,
      contentType: String
  }
});
module.exports = mongoose.model('Image', imageSchema)
