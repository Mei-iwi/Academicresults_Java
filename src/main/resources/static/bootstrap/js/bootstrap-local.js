/* bootstrap-local.js - minimal interactive helpers for this static prototype. */
document.addEventListener('click',function(e){
  const trigger=e.target.closest('[data-bs-toggle="dropdown"]');
  if(trigger){e.preventDefault(); const menu=trigger.parentElement.querySelector('.dropdown-menu'); if(menu) menu.classList.toggle('show');}
});
