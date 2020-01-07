const mongoose = require('mongoose');
const Schema = mongoose.Schema;

/**
 * Create Schema
 */
const contactSchema = new Schema({
    name: {
        type: String,
        default: ""
    },
    phoneNumber: {
        type: String,
        default: ""
    },
    email: {
        type: String,
        default: ""
    },
    thumbnail: {
        type: String,
        default: ""
    },
    localCached: {
        type: Boolean,
        default: false
    }
});

module.exports = mongoose.model('Contact', contactSchema)