import {Button, Col, Container, FormControl, FormLabel, Modal, Row} from "react-bootstrap";
import {useState} from "react";
import {useMutation, useQueryClient} from "react-query";
import KeyService from "../../services/KeyService";

export const AddKeyModal = (props) => {
    const [build, setBuild] = useState("")
    const [room, setRoom] = useState("")


    const a = props.keys.map((key) => {
        return {build: key.build, room: key.room}
    })


    const queryClient = useQueryClient()

    const mutation = useMutation((keyParams) => KeyService.addKey(keyParams.build, keyParams.room), {
        onSuccess() {
            setBuild("")
            setRoom("")
            queryClient.invalidateQueries(['keys'])
        },
        onError(){
            alert('Для данного ключа нет аудитории')
        }
    })


    const handleCreateKey = () => {
        if (a.some(key => key.build == build && key.room == room)){
            alert('Такой ключ уже существует')
            return
        }
        mutation.mutate({build, room})
        props.onHide()
    }

    return (
        <Modal {...props} size={"lg"} centered>
            <Modal.Header closeButton>
                <Modal.Title>
                    Создание ключа
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Container>
                    <Row className='d-flex'>
                        <Col className='col-6'>
                            <FormLabel>Здание</FormLabel>
                            <FormControl className='col-6' onChange={(e) => setBuild(e.target.value)} value={build}
                                         placeholder='Номер здания' type='number' min={0}></FormControl>
                        </Col>
                        <Col className='col-6'>
                            <FormLabel>Аудитория</FormLabel>
                            <FormControl className='col-6' onChange={(e) => setRoom(e.target.value)} value={room}
                                         placeholder='Номер аудитории' type='number' min={0}></FormControl>
                        </Col>
                    </Row>
                    <Row>
                        <Col className=' mt-3 d-flex justify-content-end'>
                            <Button onClick={handleCreateKey} className='w-25'>Создать</Button>
                        </Col>
                    </Row>
                </Container>
            </Modal.Body>
            <Modal.Footer>

            </Modal.Footer>
        </Modal>
    )
}