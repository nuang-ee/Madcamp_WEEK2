const mongoose = require ('mongoose');
const claimerSchema = new mongoose.Schema({
	claimee: String,		// 받을 상대 이름
	amount: Number,			// 받을 금액
	name: String,				// 거래 이름
	date: String, 			// 거래 생성 날짜
	received: Boolean		// 돈을 받았는지 여부
})

module.exports = mongoose.model('claimer', claimerSchema)