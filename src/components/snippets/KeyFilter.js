import {
    Button,
    Card,
    CardBody,
    CardHeader,
    CardTitle, Col, Collapse,
    Container, DropdownDivider,
    Form,
    FormCheck,
    FormControl,
    FormLabel, Row, ToggleButton
} from "react-bootstrap";
import FormCheckLabel from "react-bootstrap/FormCheckLabel";
import {useState} from "react";
import KeysStore from "../../store/keysStore";
import FormCheckInput from "react-bootstrap/FormCheckInput";

export const KeyFilter = () => {

    const [isOpen, setOpen] = useState(true)

    const [inAll, setInAll] = useState(true)
    const [inDeanery, setInDeanery] = useState(false)
    const [inHand, setInHand] = useState(false)
    const [build, setBuild] = useState(undefined)
    const [room, setRoom] = useState(undefined)

    const handleFind = (e) => {
        e.preventDefault()
        let keyStatus
        if (!inAll){
             keyStatus = inDeanery ? 'IN_DEANERY' : 'IN_HAND'
        }
        KeysStore.setKeysFilter({keysStatus:keyStatus,build:build,room:room})
    }


    const handleSwitchInAll = (e) => {
        if (e.target.value === 'on') {
            setInAll(!inAll)
            if (inAll) {
                setInHand(false)
                setInDeanery(true)
            }
        }
    }
    const handleSwitchInHand = (e) => {
        if (e.target.value === 'on') {
            setInHand(true)
            setInDeanery(false)
        } else {
            setInHand(false)
            setInDeanery(true)
        }
    }

    const handleSwitchInDeanery = (e) => {
        if (e.target.value === 'on') {
            setInDeanery(true)
            setInHand(false)
        } else {
            setInDeanery(false)
            setInHand(true)
        }
    }


    return (
        <Container
            className='mt-5'>
            <Button className='w-100 d-md-none mb-3' aria-expanded={isOpen} onClick={() => {
                setOpen(!isOpen)
            }}>
                Фильтры
            </Button>
            <Collapse in={isOpen}>
                <div>
                    <Card className='rounded-0 rounded-top-3'>
                        <CardHeader>
                            <CardTitle>Фильтр ключей</CardTitle>
                        </CardHeader>
                        <CardBody>
                            <Form className='container-fluid' onSubmit={handleFind}>
                                <Row className='d-flex align-content-between'>
                                    <Col lg={inAll ? 3 : 2} xs={6}>
                                        <FormControl min={0} onChange={(e) => {
                                            setBuild(e.target.value)
                                        }} placeholder={'Номер здания'} type='number'/>
                                    </Col>
                                    <Col lg={inAll ? 3 : 2} xs={6}>
                                        <FormControl min={0} onChange={(e) => {
                                            setRoom(e.target.value)
                                        }} placeholder={'Номер аудитории'} type='number'/>
                                    </Col>
                                    <Col lg={inAll ? 3 : 2} md={12} xs={12}>
                                        <FormCheck checked={inAll} onChange={(e) => {
                                            handleSwitchInAll(e)
                                        }} className='mt-2 mt-lg-0' type={'switch'} label={'Выбрать все'}/>
                                    </Col>
                                    {inAll === false && <>
                                        <Col lg={2}  sm={4} xs={6}>
                                            <FormCheck checked={inDeanery} onChange={(e) => {
                                                handleSwitchInDeanery(e)
                                            }} className='mt-2 mt-lg-0' type={'radio'} label={'В деканате'}/>
                                        </Col>
                                        <Col lg={2} sm={4} xs={6}>
                                            <FormCheck checked={inHand} onChange={(e) => {
                                                handleSwitchInHand(e)
                                            }} className='mt-2 mt-lg-0' type={'radio'} label={'На руках'}/>
                                        </Col>
                                    </>}
                                    <Col lg={inAll ? 2 : 2} md={4} sm={12} xs={12}>
                                        <Button className='mt-2 mt-lg-0 w-100 ms-auto' type='submit'>
                                            Найти
                                        </Button>
                                    </Col>
                                </Row>
                            </Form>
                        </CardBody>
                    </Card>
                </div>
            </Collapse>

        </Container>
    )
}