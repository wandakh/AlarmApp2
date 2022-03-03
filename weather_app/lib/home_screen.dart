import 'package:flutter/material.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  // membuat var untuk data dummy
  int temp = 0 ;
  String location = 'Jonggol' ;
  String weather ='lightrain';


  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        image: DecorationImage(
          image: AssetImage('asset/$weather.png'),
          fit: BoxFit.cover,
          colorFilter: ColorFilter.mode(Colors.black. withOpacity(0.6),BlendMode.dstATop)
        )
      ),
      child: Scaffold(
        backgroundColor: Colors.transparent,
        body: SingleChildScrollView(
          padding: EdgeInsets.only(top: 100),
          child: Column(
            children: [
              Column(
                children: [
                  Center(
                    child: Image.network(
                        'https://webstockreview.net/images/cloud-icon-png.png',
                      width: 100,),
                  )
                ],
              )
            ],
          ),
        ),
      ),
    );
  }
}
