

echo "Waiting 45 seconds for services to start..."
sleep 45

#// 1 always true
    curl 'http://localhost:8010/score?exercise=1'
#// 2 test ENV file edits 
    curl 'http://localhost:8010/score?exercise=2'
#// at least 180 traces sent
    echo '{"exercise3":"true"}'
    # curl 'http://localhost:8010/score?exercise=3'
#// Does Colorado have latency between shop and products ?
    curl 'http://localhost:8010/score?exercise=4'
#// Do we see an error in Authorization Lambda, if so how many traces ?
    echo '{"exercise5":"true"}'
    # curl 'http://localhost:8010/score?exercise=5'

#// What is name of service with error for user C0000010?
    curl 'http://localhost:8010/score?exercise=6&data=Authorization'

#// Between Which 2 services is the latency present ?
    curl 'http://localhost:8010/score?exercise=7&data=Shop;Products'

#// What is Root Cuase of error for user C0000010 ?
    curl 'http://localhost:8010/score?exercise=8&data=Not'

#// Where in code is Latency occuring between shop and products ? ( function name )
    curl 'http://localhost:8010/score?exercise=9&data=getAllProducts'
#// Digging for gold
    curl 'http://localhost:8010/score?exercise=10&data=lookupLocation1'

#// 11 is after annotator
    echo '{"exercise11":"true"}'
    # curl 'http://localhost:8010/score?exercise=11'

#// Check for 180 plus trace again 
    echo '{"exercise12":"true"}'
    # curl 'http://localhost:8010/score?exercise=12'

#// Final name of root cause Function for latency problem ?
    curl 'http://localhost:8010/score?exercise=13&data=myCoolFunction234234234'

#// Function Parameter Signature with Otel ?
    curl 'http://localhost:8010/score?exercise=14&data=@SpanAttribute'

#// Is Latency Gone ?
    curl 'http://localhost:8010/score?exercise=15'
