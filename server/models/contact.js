const mongoose = require ('mongoose');
const Schema = mongoose.Schema;

/**
 * Create Schema
 */
const contactSchema = new Schema({
    name: {
        type: String,
        required: true
    },
    phoneNumber: {
        type: String,
        required: true
    },
    email: String,
    thumbnail: String,
    localCached: {
        type: Boolean,
        default: false
    }
});

module.exports = mongoose.model ('Contact', contactSchema)