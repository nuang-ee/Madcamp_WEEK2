const mongoose = require ('mongoose');
const Schema = mongoose.Schema;

/**
 * Create Schema
 */
const contactSchema = new Schema({
    name: String,
    phoneNumber: String,
    email: String,
    thumbnail: String,
    localCached: {
        type: Boolean,
        default: false
    }
});

module.exports = mongoose.model ('Contact', contactSchema)