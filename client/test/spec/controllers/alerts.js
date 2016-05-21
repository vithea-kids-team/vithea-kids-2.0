'use strict';

describe('Controller: AlertsCtrl', function () {

  // load the controller's module
  beforeEach(module('clientApp'));

  var AlertsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AlertsCtrl = $controller('AlertsCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(AlertsCtrl.awesomeThings.length).toBe(3);
  });
});
