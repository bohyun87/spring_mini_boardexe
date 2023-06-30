// 휴대폰번호 유효성 검사
function isValidPhone(tel) {
	var format = /^010-?([0-9]{4})-?([0-9]{4})$/;
	if(tel.search(format) != -1)
		return true;
	return false;
}

