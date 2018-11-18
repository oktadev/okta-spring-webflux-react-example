function subscribeToProfiles(callback: any) {
  const socket = io('localhost:3000', {
    path: '/ws/profiles',
    /*transportOptions: {
      polling: {
        extraHeaders: {
          'Authorization': 'Bearer ' + accessToken
        }
      }
    }*/
  });
  socket.on('timer', (timestamp: any) => callback(null, timestamp));
  socket.emit('subscribeToProfiles', 1000);
}
export { subscribeToProfiles };