import {KeysItem} from "./key/keyItem";
import {KeyFilter} from "../../snippets/KeyFilter";
import {Button, Card, CardBody, CardHeader, CardTitle, Container} from "react-bootstrap";
import {useQuery} from "react-query";
import KeysStore from "../../../store/keysStore";
import {observer} from "mobx-react-lite";
import {Loading} from "../../snippets/Loading";
import {AddKeyModal} from "../../snippets/AddKeyModal";
import {useState} from "react";


export const KeysList = observer(() => {

    const [addKeyModal, setAddKeyModal] = useState(false)

    const {
        data,
        isLoading,
        error
    } = useQuery(['keys', KeysStore.keys_status, KeysStore.build, KeysStore.room], () => KeysStore.getKeys(), {
        refetchOnWindowFocus: false,
        keepPreviousData: true
    })

    if (isLoading) {
        return (
            <Loading/>
        )
    }

    if (error) {
        return (
            <p>Ошибка</p>
        )
    }

    // if (!data.data.length) {
    //     return (
    //         <>
    //             <Container className='mt-5'>
    //                 <Button onClick={() => setAddKeyModal(true)} className='btn-success'>Создать ключ</Button>
    //             </Container>
    //             <AddKeyModal show={addKeyModal} onHide={() => setAddKeyModal(false)}/>
    //             <KeyFilter/>
    //             <Container className='text-center text-danger fs-1 fw-bold mt-3'>Нет ключей</Container>
    //         </>
    //     )
    // }
    return (
        <>
            <Container className='mt-5'>
                <Button onClick={() => setAddKeyModal(true)} className='btn-success'>Создать ключ</Button>
            </Container>
            <KeyFilter/>
            <AddKeyModal show={addKeyModal} onHide={() => setAddKeyModal(false)
            }/>
            {data.data.length
                ?
                <>
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
                :
                <>
                    <Container className='text-center text-danger fs-1 fw-bold mt-3'>Нет ключей</Container>
                </>
            }

        </>
    )
})