const mongoose = require('mongoose');
const imageSchema = new mongoose.Schema(
  { 
    contentUrl: String,
    localCached: {
      type: Boolean,
      default: false
    }
  }
);

module.exports = mongoose.model('Image', imageSchema)
