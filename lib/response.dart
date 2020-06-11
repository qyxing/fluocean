typedef OceanResponse _ResponseInvoker(Map argument);

Map<String, _ResponseInvoker> _responseMapper = {
  "onResponse": (Map argument) => OceanResponse.fromMap(argument),
  "onRewardResponse": (Map argument) => RewardResponse.fromMap(argument),
};

class OceanResponse {
  final int code;
  final String message;

  bool get isSuccessful => code == 0;

  OceanResponse._(this.code, this.message);

  factory OceanResponse.create(String name, Map argument) =>
      _responseMapper[name](argument);

  OceanResponse.fromMap(Map map)
      : code = map["code"],
        message = map["message"];
}

class RewardResponse extends OceanResponse {
  final bool verify;
  final int amount;
  final String name;

  RewardResponse.fromMap(Map map)
      : verify = map["verify"],
        amount = map["amount"],
        name = map["name"],
        super._(map['code'], map['message']);
}
