import {KeysItem} from "./key/keyItem";
import {KeyFilter} from "../../snippets/KeyFilter";
import {Card, CardBody, CardHeader, CardTitle, Container} from "react-bootstrap";
import {useQuery} from "react-query";
import KeysStore from "../../../store/keysStore";


// нужен запрос
const keysData = [
    {keyNumber: 111, hasRequests: true, location: 'в деканате'},
    {keyNumber: 222, hasRequests: false, location: 'на руках y Rjuj-nj'},
    {keyNumber: 222, hasRequests: false, location: 'на руках y Rjuj-njsrtrtettr'},
    {keyNumber: 333, hasRequests: true, location: 'в деканате'}
];

export const KeysList = () => {

    const {data, isLoading, error} = useQuery('getKeys', async () => {
        await KeysStore.getKeys()
    })

    return (
        <>
            <KeyFilter/>
            <Container className="mt-3 border-0">
                <Card className='rounded-0'>
                    <CardBody>
                        {keysData.map((keyData, index) => (
                            <KeysItem
                                key={index}
                                keyNumber={keyData.keyNumber}
                                hasRequests={keyData.hasRequests}
                                location={keyData.location}
                            />
                        ))}
                    </CardBody>
                </Card>
            </Container>
        </>
    )
}