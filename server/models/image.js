const mongoose = require('mongoose');
const imageSchema = new mongoose.Schema({
  contentUrl: {
    type: String,
    default: ""
  },
  localCached: {
    type: Boolean,
    default: false
  }
});

module.exports = mongoose.model('Image', imageSchema)