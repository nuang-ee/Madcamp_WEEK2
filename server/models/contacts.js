const mongoose = require ('mongoose');
const Schema = mongoose.Schema;

/**
 * Create Schema
 */
const contactSchema = new Schema({
    _id: {
        type: Number,
        required: "_id is essential"
    },
    phone_number: String,
    email: String,
    thumbnail: { data: Butter, contentType: String }
});

module.exports = mongoose.model ('Contact', contactSchema)