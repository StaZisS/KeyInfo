import {KeysItem} from "./key/keyItem";
import {KeyFilter} from "../../snippets/KeyFilter";
import {Card, CardBody, CardHeader, CardTitle, Container} from "react-bootstrap";


// нужен запрос
const keysData = [
    {keyNumber: 111, hasRequests: true, location: 'в деканате'},
    {keyNumber: 222, hasRequests: false, location: 'на руках y Rjuj-nj'},
    {keyNumber: 222, hasRequests: false, location: 'на руках y Rjuj-njsrtrtettr'},
    {keyNumber: 333, hasRequests: true, location: 'в деканате'}
];

export const KeysList = () => {
    return (
        <>
            <KeyFilter/>
            <Container className="mt-3 border-0">
                <Card className='rounded-0'>
                    <CardHeader>
                        <CardTitle>Список ключей</CardTitle>
                    </CardHeader>
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