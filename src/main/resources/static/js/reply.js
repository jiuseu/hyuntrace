async function getReply(rno){
   const res = await axios.get(`/api/reply/${rno}`,rno)
   return res.data
}

async function addReply(replyDTO){
  const res = await axios.post(`/api/reply/register`, replyDTO)
  return res.data
}

async function modifyReply(replyDTO){
  const rno = replyDTO.rno
  const res = await axios.put(`/api/reply/${rno}`, replyDTO)
  return res.data
}

async function removeReply(rno){
  const res = await axios.delete(`/api/reply/${rno}`,rno)
  return res.data
}

async function getReplyList({bno,page,size,goLast}){

  const res = await axios.get(`/api/reply/list/${bno}`,{params:{page,size}})

  if(goLast){ // 마지막 페이지 즉 최신 댓글페이지부터 조회할 수 있도록 하고 싶으면 참거짓을 넣어서 page를 끝페이지로 설정해서 넣는다
    const total = res.data.total
    const lastPage = parseInt(Math.ceil(total/size))

    return getReplyList({bno:bno,page:lastPage,size:size})
  }
  return res.data
}
