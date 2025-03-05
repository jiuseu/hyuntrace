document.addEventListener('DOMContentLoaded', function() {
    console.log("회원가입 스크립트 로드됨");

    document.addEventListener("click", function(e) {
        if (e.target && e.target.classList.contains("registerBtn")) {
            console.log("회원가입 버튼 클릭됨");

            const mid = document.querySelector('input[name="mid"]').value;
            const mpw = document.querySelector('input[name="mpw"]').value;
            const email = document.querySelector('input[name="email"]').value;
            const memberJoinDTO = { mid: mid, mpw: mpw, email: email };
            console.log("전송 데이터:", memberJoinDTO);

            // 서버로 회원가입 요청
            axios.post(`/api/member/join`, memberJoinDTO, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(res => {
                console.log("회원가입 성공");
                self.location = `/member/login`;
            })
            .catch(err => {
                console.error("회원가입 실패:", err);
                if (err.response) {
                    let errMsg = Object.entries(err.response.data)
                        .map(([field, message]) => `${field} : ${message}`).join("\n");
                    alert(errMsg);
                }
            });
        }
    });
});
