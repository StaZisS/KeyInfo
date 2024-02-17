import {KeysItem} from "./key/keyItem";
import {KeyFilter} from "../../snippets/KeyFilter";
import {Card, CardBody, CardHeader, CardTitle, Container} from "react-bootstrap";
import {useQuery} from "react-query";
import KeysStore from "../../../store/keysStore";
import {observer} from "mobx-react-lite";
import {Loading} from "../../snippets/Loading";


export const KeysList = observer(() => {

    const {
        data,
        isLoading,
        error
    } = useQuery(['getKeys', KeysStore.keys_status, KeysStore.build, KeysStore.room], () => KeysStore.getKeys(), {
        refetchOnWindowFocus: true,
        //keepPreviousData: true
    })

    if (isLoading) {
        return (
            <Loading/>
        )
    }

    if (error) {
        return (
            <p>Ошибка: {error.message}</p>
        )
    }

    if (!data.data.length) {
        return (
            <>
                <KeyFilter/>
                <Container className='text-center text-danger fs-1 fw-bold mt-3'>Нет ключей</Container>
            </>
        )
    }
    return (
        <>
            <KeyFilter/>
            <Container className="mt-3 border-0">
                <Card className='rounded-0'>
                    <CardBody>
                        {data.data.map((key) => (
                            <KeysItem
                                key={key.key_id}
                                keyNumber={key.room}
                                //hasRequests={key.hasRequests}
                                //location={key.location}
                            />
                        ))}
                    </CardBody>
                </Card>
            </Container>
        </>
    )
})