async function uploadToServer(formObj){
   const response = await axios({
      method : 'post',
      url : '/api/upload/post',
      data : formObj,
      headers : {'Content-Type':'multipart/form-data',},
   });
   return response.data
}

async function removeFileToServer({uuid, fileName}){
    const response = await axios.delete(`/api/upload/remove/${uuid}_${fileName}`)
    return response.data
}