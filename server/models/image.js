const mongoose = require('mongoose');
const imageSchema = new mongoose.Schema(
  { 
    contentUrl: {
      type: String,
      required: true
    },
    localCached: {
      type: Boolean,
      default: false
    }
  }
);

module.exports = mongoose.model('Image', imageSchema)
