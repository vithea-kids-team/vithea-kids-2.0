'use strict';

describe('Controller: ListchildrenCtrl', function () {

  // load the controller's module
  beforeEach(module('clientApp'));

  var ListchildrenCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ListchildrenCtrl = $controller('ListchildrenCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
