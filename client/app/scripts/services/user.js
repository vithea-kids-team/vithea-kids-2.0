'use strict';

/**
 * @ngdoc service
 * @name clientApp.user
 * @description
 * # user
 * Service in the clientApp.
 */
angular.module('clientApp')
   .factory('userService', function() {
   	// AngularJS will instantiate a singleton by calling "new" on this function
     var username = '';

     return {
       username : username
     };
   });
